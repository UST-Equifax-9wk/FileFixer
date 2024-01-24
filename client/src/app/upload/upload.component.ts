import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { RemoteService } from '../remote.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-upload,',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent {

  fileName = '';
  specFileName = '';
  fileSize = 0;
  count = 0;
  formData = new FormData();
  responseData = '';
  responseText = '';
  fileLayout = '';
  fileSizeError = '';
  errorMessage = '';
  options: string[] = ['Person', 'Person with Long Data', 'Car', 'Car with Long Data', 'Custom'];
  optionMapper: { [key: string]: string; } = {
    'Person' : 'PERSON',
    'Car' : 'CAR' ,
    'Car with Long Data' : 'CAR2',
    'Person with Long Data' : 'PERSON2',
    'Custom' : 'CUSTOM'
  };


  constructor(private http: HttpClient, private remoteService: RemoteService) {}

  private resetFormData(): void {
    this.formData = new FormData();
    this.fileName = '';
    this.specFileName = '';
    this.fileSizeError = '';
  }

  private resetResponseData(): void {
    this.responseData = '';
    this.responseText = '';
    this.errorMessage = '';
  }

  private isFileValid(): boolean {
    if (this.fileSize > 2) {
      this.fileSizeError = 'File size must be less than 2 MB.';
      return false;
    }
    return true;
  }


  onFileSelected(event: any): void {
    this.resetResponseData();
    this.resetFormData();
    const file: File = event.target.files[0];
    if (file) {
      this.fileName = file.name;
      this.isFileValid();
      this.formData.append("flatFile", file);
    } 
  }

  onCustomSelected(event: any) {
    this.resetResponseData();
    const file: File = event.target.files[0];
    if (file) {
      this.specFileName = file.name;
      this.isFileValid();
      this.formData.append("specJSON", file);
    } 
  }

  selectUploadFile(): void {
    this.fileSizeError = '';
    this.resetResponseData();
    this.remoteService.uploadFile(this.formData,  this.optionMapper[this.fileLayout])
    .subscribe({
      next: (data) => {
        this.responseData = JSON.stringify(data.body, null, 2);
        let response = JSON.parse(this.responseData);
        this.count = response.length;
        this.responseText = response.length > 5 ? JSON.stringify(response.slice(0, 3), null, 2) : this.responseData;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error;
      }
    })
  }

  saveData() {
    var a = document.createElement("a");
    document.body.appendChild(a);
    var json = this.responseData,
        blob = new Blob([json], {type: "octet/stream"}),
        url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = this.fileName.split('.')[0] + ".json";
    a.click();
    window.URL.revokeObjectURL(url);
  }


}
